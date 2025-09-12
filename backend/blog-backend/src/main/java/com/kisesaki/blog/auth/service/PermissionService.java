package com.kisesaki.blog.auth.service;

import org.springframework.stereotype.Service;

import com.kisesaki.blog.auth.mapper.PermissionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionMapper permissionMapper;
}
