package com.todolists.alltests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.todolists.handlers.CloseAllProjectsHandlerTest;
import com.todolists.handlers.CloseProjectHandlerTest;
import com.todolists.handlers.OpenProjectHandlerTest;
import com.todolists.handlers.SaveTableHandlerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CloseAllProjectsHandlerTest.class, CloseProjectHandlerTest.class, SaveTableHandlerTest.class,
		OpenProjectHandlerTest.class })
public class AllTests {

}