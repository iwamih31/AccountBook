package com.iwamih31;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspct {

	// AOP実装（メソッドが実行される前に、Advice実行）
	// クラス名の最後に"Controller"が付くクラスの全てのメソッドをAOPの対象にする
	@Before("execution(* *..*.*Controller.*(..))")
	public void startLog_Controller(JoinPoint jp){
		System.out.println("");
	  System.out.println("メソッド開始： " + jp.getSignature());
	}

	// AOP実装（メソッドが実行された後に、Advice実行）
	// クラス名の最後に"Controller"が付くクラスの全てのメソッドをAOPの対象にする
	@After("execution(* *..*.*Controller.*(..))")
	public void endLog_Controller(JoinPoint jp) {
	  System.out.println("メソッド終了： " + jp.getSignature());
	  System.out.println("");
	}


	// AOP実装（メソッドが実行される前に、Advice実行）
	// クラス名の最後に"Service"が付くクラスの全てのメソッドをAOPの対象にする
	@Before("execution(* *..*.*Service.*(..))")
	public void startLog_Service(JoinPoint jp){
		System.out.println("");
		System.out.println("メソッド開始： " + jp.getSignature());
	}

	// AOP実装（メソッドが実行された後に、Advice実行）
	// クラス名の最後に"Service"が付くクラスの全てのメソッドをAOPの対象にする
	@After("execution(* *..*.*Service.*(..))")
	public void endLog_Service(JoinPoint jp) {
		System.out.println("メソッド終了： " + jp.getSignature());
		System.out.println("");
	}


	// AOP実装（メソッドが実行される前に、Advice実行）
	// クラス名の最後に"Excel"が付くクラスの全てのメソッドをAOPの対象にする
	@Before("execution(* *..*.*Excel.*(..))")
	public void startLog_Excel(JoinPoint jp){
		System.out.println("");
		System.out.println("メソッド開始： " + jp.getSignature());
	}

	// AOP実装（メソッドが実行された後に、Advice実行）
	// クラス名の最後に"Excel"が付くクラスの全てのメソッドをAOPの対象にする
	@After("execution(* *..*.*Excel.*(..))")
	public void endLog_Excel(JoinPoint jp) {
		System.out.println("メソッド終了： " + jp.getSignature());
		System.out.println("");
	}

}