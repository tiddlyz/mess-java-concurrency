Test Case: testWaitAndNotify()

说明：用`wait()`, `notify()`, `synchronized()` 协调线程工作。主线程（测试线程）用`wait/notify/synchronized` 保证SyncThread完成计算后再输出计算结果。

问题说明：

[正确输出]

Possible output 1: 
1. 主线程调用`t.wait()`是current thread 挂起等待，进入sleep
2. `t` 线程完成计算后，调用`notify()`使在t的waiting set中的线程激活
3. 主线程激活后继续运行输出正确结果。

[错误]

Possible output 1: 
1. 在主线程进入critical section之前（synchronized statement），`t`有可能已进入critical section （synchronized statement）。
2. 此时，只有线程 `t`完成critical section后，主线程才可进入critical section，并执行`wait()`释放锁并sleep。
3. 但在主线程进入critical section之前，线程`t` 已经执行了`notify()`(退出`t`的synchronized statement).
4. 主线程在执行`wait()`后，将不再会有其他线程调用`t.notify()`唤醒主线程了。
5. 所以主线程进入了无限等待中。


