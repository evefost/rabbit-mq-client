	
	PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
	
	
	1.beandefinetion 注册完成
	ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry
	2.创建properties实例
	 自定义BeanDefinitionRegistryPostProcessor
	 获取所有properties(没注入属性值)
	 注入属性值
	3.创建cleint
	
	
	
	接收消息
	SimpleMessageListenerContainer#doReceiveAndExecute
	自动添加tenantId实现方式
	1.添加MessagePostProcessor 解释paylod 后，重新生成新的payload
	2.spring aop  解释paylod 后，重新生成新的payload
	3.字节码式，接直修改来
	都必须获取方法参数类型
	