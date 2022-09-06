package com.example.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CryptoUtilTest {
    private CryptoUtil cryptoUtil;

    @BeforeEach
    void init() {
        cryptoUtil = new CryptoUtil();
    }

    @Test
    public void password를_암호화한_후에_암호화된_password를_return한다() {
        // given
        String password = "123456";

        // when
        String encryptedPassword = cryptoUtil.encryptPassword(password);

        // then
        assertThat(encryptedPassword).isInstanceOfAny(String.class);
        assertThat(encryptedPassword).isNotEqualTo(password);
    }

    @Test
    public void 암호화된_password가_기존_password와_같다면_true를_return한다() {
        // given
        String password = "123456";
        String encryptedPassword = cryptoUtil.encryptPassword(password);

        // when
        Boolean result = cryptoUtil.validatePassword(password, encryptedPassword);

        // then
        assertThat(result).isTrue();
    }
}