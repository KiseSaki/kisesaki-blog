package com.kisesaki.blog.common.util;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 通用工具类
 *
 * @author KiseSaki
 */
@Component
public class Utils {

    /**
     * 根据用户ID生成哈希目录路径
     *
     * @param userId 用户ID
     * @return 哈希目录路径 (格式: xx/yy)
     */
    public String getHashedDirectoryByUserId(Long userId){
        // murmur3_32_fixed: 32位的MurmurHash3哈希算法，适用于非加密用途的快速哈希计算
        // StandardCharsets.UTF_8: 指定使用UTF-8字符集进行字符串编码
        String hash = Hashing.murmur3_32_fixed().hashString(userId.toString(), StandardCharsets.UTF_8).toString();

        // 取哈希值的前四位，创建两级目录，每级目录两位字符
        String level1 = hash.substring(0, 2);
        String level2 = hash.substring(2, 4);
        return String.format("%s%s", level1, level2);
    }
}
