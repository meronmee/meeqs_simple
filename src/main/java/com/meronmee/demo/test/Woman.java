package com.meronmee.demo.test;

import org.springframework.stereotype.Service;

@Service  
public class Woman implements Human {  
  
    public void speak() {  
        System.out.println(" woman speaking!");  
  
    }  
  
    public void walk() {  
        System.out.println(" woman walking!");  
  
    }  
  
}  