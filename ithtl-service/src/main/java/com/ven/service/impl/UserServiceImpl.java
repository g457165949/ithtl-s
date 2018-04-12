package com.ven.service.impl;

import com.ven.model.account.User;
import com.ven.repo.UserRepository;
import com.ven.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User account) {
        userRepository.save(account);
        return account;
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public Page<User> findAll(User user, Pageable pageable) {
		Specification<User> spec = new Specification<User>() {       //查询条件构造

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(user.getUsername())) {
                    list.add(cb.like(root.get("username").as(String.class), "%" + user.getUsername() + "%"));
                }

                if (StringUtils.isNotBlank(user.getNickname())) {
                    list.add(cb.like(root.get("nickname").as(String.class), "%" + user.getNickname() + "%"));
                }

                Predicate[] p = new Predicate[list.size()];
                if(p.length>0) {
                    query.where(cb.or(list.toArray(p)));
                }

                return query.getRestriction();
            }
        };
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(id);
    }

    @Override
    public User dynamicSave(Integer id, User user) {
        return userRepository.dynamicSave(id, user);
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }
}
