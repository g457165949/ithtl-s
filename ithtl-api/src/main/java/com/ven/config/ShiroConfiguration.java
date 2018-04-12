package com.ven.config;

//import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.ven.filter.CaptchaFilter;
import com.ven.filter.KickoutSessionControlFilter;
import com.ven.filter.MyFormAuthenticationFilter;
import com.ven.model.system.SysPermission;
import com.ven.service.SysPermissionService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro 配置 Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。
 * 既然是使用 Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
 * 
 */
@Configuration
public class ShiroConfiguration {

	@Resource
	private SysPermissionService permissionService;

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
	 *
	 * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 *
	 */
	@Bean
	public ShiroFilterFactoryBean shirFilter() {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager());

		// 添加自定义过滤器
		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		filters.put("captchaFilter", new CaptchaFilter());
		filters.put("authc",new MyFormAuthenticationFilter());
		filters.put("user", new MyUserFilter());
		filters.put("kickout",kickoutSessionControlFilter());
//		filters.put("authc",new MyPassThruAuthenticationFilter());

		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/", "user");
		filterChainDefinitionMap.put("/captcha", "anon");   	//验证码
		 // 配置退出过滤器
//        filterChainDefinitionMap.put("/site/logout", "authc");
		filterChainDefinitionMap.put("/site/login", "captchaFilter,authc");  //认证和验证码过滤器

		// 自定义加载权限资源关系
        List<SysPermission> permissionList = permissionService.findAll();
        for(SysPermission permissionInfo:permissionList){
            if (StringUtils.isNotBlank(permissionInfo.getUrl())) {
                String permission = "perms[" + permissionInfo.getPerms()+ "]";
                System.out.println("permission:"+permission);
                filterChainDefinitionMap.put(permissionInfo.getUrl(),permission);
            }
        }

        // 如果不设置默认会自动寻找Web工程根目录下的"/login"页面
 		shiroFilterFactoryBean.setLoginUrl("/site/login");
 		// 登录成功后要跳转的链接
// 		shiroFilterFactoryBean.setSuccessUrl("/");
 		// 未授权界面;
 		shiroFilterFactoryBean.setUnauthorizedUrl("/err");
 		// 其他需要登录
 		filterChainDefinitionMap.put("/**","kickout,user");
//		filterChainDefinitionMap.put("/**","authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	/**
	 * 安全管理器
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSessionManager(sessionManager());
		securityManager.setCacheManager(cacheManager());
		securityManager.setRealm(myShiroRealm());
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 记住我cookie管理;
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager(){
		System.out.println("ShiroConfiguration.rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		//rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
		return cookieRememberMeManager;
	}

	/**
	 * 记住我cookie设置
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		System.out.println("ShiroConfiguration.rememberMeCookie()");
		//这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		//<!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * 自定义session
	 * @return
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager(){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// session的失效时长，单位毫秒
		sessionManager.setGlobalSessionTimeout(600000);
		// 删除失效的session
		sessionManager.setDeleteInvalidSessions(true);
		// cache
		EnterpriseCacheSessionDAO cacheSessionDAO = new EnterpriseCacheSessionDAO();
		cacheSessionDAO.setCacheManager(cacheManager());
		sessionManager.setSessionDAO(cacheSessionDAO);
		return sessionManager;
	}

	/**
	 * 单点登录过滤器
	 * @return
	 */
	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter(){
		System.out.println("ShiroConfiguration.kickoutSessionControlFilter()");
		KickoutSessionControlFilter filter = new KickoutSessionControlFilter();
		filter.setSessionManager(sessionManager());
		filter.setCacheManager(cacheManager());
		filter.setKickoutAfter(false);
		filter.setMaxSession(1);
		filter.setKickoutUrl("/site/login?kickout=1");
		return filter;
	}

	/**
	* shiro缓存管理器;
	* 需要注入对应的其它的实体类中：
	* 1、安全管理器：securityManager
	* 可见securityManager是整个shiro的核心；
	* @return
	*/
   @Bean(name = "cacheManager")
   public EhCacheManager cacheManager(){
      System.out.println("ShiroConfiguration.getEhCacheManager()");
      EhCacheManager cacheManager = new EhCacheManager();
      cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
      return cacheManager;

   }

	/**
	 * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
	 * @return
	 */
	@Bean(name = "shiroRealm")
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCachingEnabled(true);
		myShiroRealm.setAuthorizationCacheName("authorizationCache");
		myShiroRealm.setAuthorizationCachingEnabled(true);
		myShiroRealm.setAuthenticationCacheName("authenticationCache");
		myShiroRealm.setAuthenticationCachingEnabled(true);
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	/**
	 * shiro 模板标签
	 * @return
	 */
//	@Bean(name = "shiroDialect")
//    public ShiroDialect shiroDialect() {
//        return new ShiroDialect();
//    }

	/**
	 * 使用密码盐方式
	 * @return
	 */
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
		credentialsMatcher.setHashIterations(3);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	/**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
	@Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

	/**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }
}
