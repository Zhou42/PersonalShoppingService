package com.futureshop.util;

import com.futureshop.common.Const;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yangzhou on 2017-12-31.
 */

// 这个工具类相对于iMooc上那个要简化很多。有空细看看
public class MD5Util {

    // https://www.dexcoder.com/selfly/article/4026
    public static String getMD5(String str) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // add salt
            md.update((Const.MD5_SALT + str).getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5 出错");
        }
    }
}
