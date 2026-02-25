# Lab 3 / Lab 4 Android Retrofit & RecyclerView Report

## Objective
The core objective of this lab is to transform a simple demo app that displays a single movie's information into a fully functional application that displays a list of the "Top Rated Movies." This application utilizes **Retrofit** for network data fetching, **RecyclerView** for list presentation, and **Picasso** for loading movie posters.

## Core Implementation Steps

### 1. Defining the Data Model (`TopRatedResponse.java`)
Based on the JSON structure returned by the TMDB API `movie/top_rated` endpoint, we created the `TopRatedResponse` class, which contains a `results` list of `Movie` objects.
- The `@SerializedName("results")` annotation is used to map the JSON array to our Java list.

### 2. Configuring the Network Interface (`MovieApiService.java`)
In the Retrofit service interface, a new `@GET("movie/top_rated")` method was added to initiate the network request and return a `Call<TopRatedResponse>`.

### 3. Implementing the Adapter (`MovieListAdapter.java`)
An adapter extending `RecyclerView.Adapter` was created to bind the fetched movie data to the UI defined in `movie_row.xml`.
- Inside `onBindViewHolder`, the movie title, release date, vote average, and overview are bound to their respective TextViews.
- **Key Implementation**: The full poster URL is manually constructed by concatenating the base image URL (`https://image.tmdb.org/t/p/w500`) with the `poster_path` returned by the API. The **Picasso** library (`Picasso.get().load(url).into(imageView)`) is then used to asynchronously load the image into the ImageView.

### 4. Binding View and Data (`MovieListActivity.java`)
- The RecyclerView is initialized in the Activity's `onCreate` method, and a `LinearLayoutManager` is assigned.
- A `Retrofit` instance is built to make the asynchronous API call (`call.enqueue()`) to fetch the top-rated movies.
- Upon a successful response (`onResponse`), the movie data is extracted and passed to the `MovieListAdapter` to render the list.

## Key Technical Points & Notes
- **RESTful Networking**: Retrofit gracefully encapsulates RESTful API calls through interface definitions and annotations.
- **Decoupled List Presentation**: Utilizing the ViewHolder pattern in RecyclerView effectively decouples data from views, improving scrolling performance and memory efficiency for dynamic lists.
- **API Key Management**: A placeholder for the TMDB API Key is included in the project. The user must insert their own Key before demonstration and ensure it is removed prior to final submission to TAs.

## Verification
Upon running the application, the UI successfully displays a scrollable list of 20 top-rated movies from TMDB. All posters are properly loaded and cached, fully meeting the lab's acceptance criteria.
