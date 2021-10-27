# news-app
Simple app to read news and save them to read later 

**News**

<a href="https://github.com/raheemadamboev/news-app/blob/master/app-debug.apk">Demo app</a>

This is app is to read news. News can be saved to database. News are from <a href="https://newsapi.org/">News API</a>. You can search for news and pagination is properly handled. App is in MVVM framework so it can resist configuration change. Also Jetpack Navigation Component and Hilt Dependency Injection is used. ViewBinding for views. 
API is in free mode, so it can stop sending news if request limit is meet. If so try again later. I removed my API Key from code so make sure to insert yours if you want to use code.

**Screenshots:**

<img src="https://github.com/raheemadamboev/news-app/blob/master/News.jpg" alt="Italian Trulli" width="869" height="416">

**Do not forget to add your NEWS API KEY to MyApi object in order to use code**

**Tech stack:**

- Dependency Injection (Hilt)
- MVVM
- Jetpack Navigation Component
- Retrofit
- Glide
- Kotlin Coroutines
- View binding
- Room
- Git
- Material Design
- REST API
- Material Design
- Pagination
- ListAdapter with DiffUtill
- Internet Check
- Handling configuration changes
