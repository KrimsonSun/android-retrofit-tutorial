# Lab 3 / Lab 4 Android Retrofit & RecyclerView 实验报告

## 实验目标
本实验的核心任务是将一个仅显示单部电影信息的 Demo，改造成一个可以获取并显示“评分最高电影列表”的完整应用。该应用需使用 **Retrofit** 进行网络请求获取数据，通过 **RecyclerView** 展示电影列表，并利用 **Picasso** 加载和显示电影海报。

## 核心实现步骤

### 1. 定义数据响应模型 (`TopRatedResponse.java`)
根据 TMDB API `movie/top_rated` 接口返回的 JSON 结构，我们创建了 `TopRatedResponse` 类，其中包含一个由 `Movie` 对象组成的 `results` 列表。
- 使用了 `@SerializedName("results")` 注解与 JSON 字段对齐。

### 2. 配置网络接口 (`MovieApiService.java`)
在 Retrofit 服务接口中，添加了一个新的 `@GET("movie/top_rated")` 方法，用于发起网络请求并返回包含电影列表的 `Call<TopRatedResponse>` 对象。

### 3. 实现适配器 (`MovieListAdapter.java`)
实现了一个继承自 `RecyclerView.Adapter` 的适配器，负责将获取到的电影数据绑定到 `movie_row.xml` 定义的 UI 上。
- 在 `onBindViewHolder` 中绑定了电影标题、发布日期、评分和简介。
- **关键技术**：手动拼接真实的图片 URL 前缀 (`https://image.tmdb.org/t/p/w500`) 和 API 返回的 `poster_path`。之后使用 **Picasso** 库 (`Picasso.get().load(url).into(imageView)`) 异步加载图片。

### 4. 绑定视图与数据 (`MovieListActivity.java`)
- 在 Activity 的 `onCreate` 方法中初始化了 RecyclerView，并设置了 `LinearLayoutManager`。
- 构建了 `Retrofit` 实例并发起异步请求 (`call.enqueue()`) 获取 top-rated 电影列表。
- 在成功的回调 (`onResponse`) 中，提取电影数据流并传递给 `MovieListAdapter`，完成列表渲染。

## 关键技术点与注意事项
- **REST 风格网络请求**：Retrofit 通过接口定义和注解优雅地封装了 RESTful API 调用。
- **解耦的列表展示**：利用 RecyclerView 的 ViewHolder 模式，有效解耦了数据与视图，提高了多条目列表滑动的性能和内存利用率。
- **API Key**：项目中预留了 API Key 的占位符（需要在 Demo 运行前填入自己的 Key），在提交代码给助教前应确保清除个人 Key 以防泄露。

## 测试与验收
应用运行后，成功在界面上展示了 TMDB 评分最高的 20 部电影，并且海报全部正确加载，列表滑动流畅，完全符合验收标准。
