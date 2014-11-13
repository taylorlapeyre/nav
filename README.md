![nav](http://i.imgur.com/dg9y1Kl.jpg)

# nav
[![Build Status](https://travis-ci.org/taylorlapeyre/nav.svg)](https://travis-ci.org/taylorlapeyre/nav)

Do you want to match URLs to handlers using Ring? Me too. That's why I made nav.

### Features
- Extremely small (< 50 lines)
- Will route URLs to handler functions
- Is easily extendable
- Is easy to understand

### Installation

Add this to your Leiningen :dependencies:

```
[nav "0.1.0"]
```

### Reasoning

[Ring][ring] might be my favorite framework. Its concepts are extraordinarily elegant, and its API is powerful.

Ring is also very minimalistic by default. By definition, your application will use just one Ring handler.

This is kind of limiting. How do we write modular code to do different things based on the URL if we only have one location to do so?

Nav breaks apart that one HTTP handler into as many as you'd like, each mapped to a specific URL and HTTP method.

### Usage

Nav is one function. The function takes a specially formatted map and turns it into a [ring][ring] handler function. That's all there is to it.

A route map looks like this:

``` clojure
(def routes {
  [:get  "/items"]    items-handler
  [:get "/items/:id"] item-handler
})
```

Of course, this might be a pain to write. So I provide some helper functions as well:

``` clojure
(use 'nav.core)
(def routes
  (-> (GET "/items"     items-handler)
      (GET "/items/:id" item-handler)))
```

The result of this is exactly equivalent to the previous code.

You may notice that you can use named url parameters. These are merged directly into the `:params` key in your request map.

Finally, to create your handler, simply pass the route map into the provided function `route` and use it in your application.

``` clojure
(ns myapp.core
  (:use org.httpkit.server
        ring.middleware.json
        ring.util.response
        nav.core))

(defn items#show
  [request]
  (let [data (json/encode {:name "routing is fun!"})]
    (ring/response data)))

(def main-routes
  (GET "/items/:id" items#show))

(def app
  (-> (route main-routes)
      (wrap-json-response)))

(run-server app {:port 8000})
```

### Contributing

1. Fork this repository
2. Create a new branch
3. Do your thing
4. Submit a pull request with a description of the change.

### License

Copyright © 2014 Taylor Lapeyre

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[ring]: https://github.com/ring-clojure/ring
