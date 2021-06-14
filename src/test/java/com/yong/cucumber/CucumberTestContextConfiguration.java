package com.yong.cucumber;

import com.yong.MovieNewsApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MovieNewsApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
