package com.meronmee.demo.test;

import org.springframework.stereotype.Service;

@Service  
public class Man implements Human {  
  
    public void speak() {  
        System.out.println(" man speaking!");  
  
    }  
  
    public void walk() {  
        System.out.println(" man walking!");  
  
    }  
  
}  