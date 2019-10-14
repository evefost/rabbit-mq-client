	
	PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
	
	
	1.beandefinetion 注册完成
	ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry
	2.创建properties实例
	 自定义BeanDefinitionRegistryPostProcessor
	 获取所有properties(没注入属性值)
	 注入属性值
	3.创建cleint