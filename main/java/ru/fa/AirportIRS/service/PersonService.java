package ru.fa.AirportIRS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fa.AirportIRS.models.Person;
import ru.fa.AirportIRS.repository.PersonRepository;
import ru.fa.AirportIRS.security.UserDetailsCustom;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Username " + username + " not found"));
        return UserDetailsCustom.build(person);
    }
    public Person getByUsername(String username) {return personRepository.findByUsername(username).orElseThrow();}
    public boolean existsByUsername(String username) {return personRepository.existsByUsername(username);}
    public boolean existsByEmail(String email) {return personRepository.existsByEmail(email);}
    public void save(Person person) {personRepository.save(person);}
}
