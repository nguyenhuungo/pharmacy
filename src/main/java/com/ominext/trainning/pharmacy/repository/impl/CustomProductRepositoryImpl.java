package com.ominext.trainning.pharmacy.repository.impl;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.model.QOrderDetail;
import com.ominext.trainning.pharmacy.model.QProduct;
import com.ominext.trainning.pharmacy.repository.CustomProductRepository;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final EntityManager entityManager;

    public CustomProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Product> findProductByCondition(SearchProductRequest searchProductRequest, Pageable pageable) {
        JPAQuery<Product> query = new JPAQuery<>(entityManager);
        BooleanBuilder where = new BooleanBuilder();
        QProduct qproduct = QProduct.product;
        where.and(qproduct.deleted.isFalse());
        if (StringUtils.isNotEmpty(searchProductRequest.getName())) {
            where.and(qproduct.name.containsIgnoreCase(searchProductRequest.getName()));
        }
        long total = query.from(qproduct).where(where).fetchCount();
        int start = (pageable.getPageNumber()) * pageable.getPageSize();
        List<Product> products = query.from(qproduct).where(where).offset(start).limit(pageable.getPageSize()).orderBy(qproduct.name.asc()).fetch();
        return new PageImpl<>(products, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }

    @Override
    public List<Product> findPopularProducts(int i) {
        JPAQuery<Product> query = new JPAQuery<>(entityManager);
        QProduct qproduct = QProduct.product;
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;

        return query.from(qproduct).join(qOrderDetail).on(qproduct.id.eq(qOrderDetail.product.id))
                .groupBy(qproduct)
                .orderBy(qOrderDetail.quantity.sum().desc())
                .limit(i).fetch();
    }


}
