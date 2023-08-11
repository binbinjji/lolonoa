package com.example.gg.member.repository;

import com.example.gg.member.domain.model.Mail;
import org.springframework.data.repository.CrudRepository;

public interface MailRepository extends CrudRepository<Mail, String> {
}
