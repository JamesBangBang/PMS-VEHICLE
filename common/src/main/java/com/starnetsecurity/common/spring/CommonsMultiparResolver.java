package com.starnetsecurity.common.spring;

/**
 * Created by 宏炜 on 2016-01-04.
 */
public class CommonsMultiparResolver extends org.springframework.web.multipart.commons.CommonsMultipartResolver {

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.web.multipart.commons.CommonsMultipartResolver#isMultipart(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean isMultipart(javax.servlet.http.HttpServletRequest request) {

        return super.isMultipart(request);
    }
}