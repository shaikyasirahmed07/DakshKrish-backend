package com.sdk.kheeti.repositories;

import com.sdk.kheeti.model.User;

public interface CustomUserRepo {
    User customFindByEmail(String email);
    User deleteUserByEmail(String email);

}
