/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.email.repositories;

import com.project.email.models.EmailModel;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 *
 * @author suzy
 */
public interface EmailRepository extends MongoRepository<EmailModel, String> {
    
}
