package com.sky.service;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface AddressBookService {
    /**
     * 根据登录用户的所有信息
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);
    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);
}
