package br.com.senai.desafio.tech_challenge.repository;
import br.com.senai.desafio.tech_challenge.model.Product;
import br.com.senai.desafio.tech_challenge.model.ProductDiscount;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class ProductSpecification {

    public static Specification<Product> hasText(String text) {
        if (!StringUtils.hasText(text)) {
            return Specification.allOf();
        }
        String lowerCaseText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("name")), lowerCaseText),
                        cb.like(cb.lower(root.get("description")), lowerCaseText)
                );
    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        if (minPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> hasDiscount(Boolean hasDiscount) {
        if (hasDiscount == null) return null;

        return (root, query, cb) -> {
            Join<Product, ProductDiscount> discountJoin = root.join("discounts", JoinType.LEFT);
            query.distinct(true);

            if (hasDiscount) {
                return cb.isNotNull(discountJoin.get("id"));
            } else {
                return cb.isNull(discountJoin.get("id"));
            }
        };
    }

    public static Specification<Product> isOutOfStock(Boolean onlyOutOfStock) {
        if (onlyOutOfStock == null || !onlyOutOfStock) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("stock"), 0);
    }

    public static Specification<Product> withCouponApplied(Boolean withCouponApplied) {
        if (withCouponApplied == null) return null;


        return (root, query, cb) -> {
            Join<Product, ProductDiscount> discountJoin = root.join("discounts", JoinType.LEFT);
            query.distinct(true);

            if (withCouponApplied) {
                return cb.isNotNull(discountJoin.get("coupon"));
            } else {
                return cb.isNull(discountJoin.get("coupon"));
            }
        };
    }
}