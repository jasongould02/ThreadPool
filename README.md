# ThreadPool

Basic thread pooling program. When initializing the ThreadPool object, the parameter in the constructor is the pool size (number of threads). In order to send tasks (objects that implement runnable), use the ThreadPool.queueTask(Runnable newTask) method.

The ThreadPool object manages a user defined number of worker threads that will request tasks from the ThreadPool task queue.
