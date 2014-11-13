(ns nav.core-test
  (:require [clojure.test :refer :all]
            [nav.core :refer :all]
            [ring.mock.request :as mock]))


(defn items#index
  [request]
  (is (= (:uri request) "/items"))
  (is (= (:request-method request) :get)))

(defn items#show
  [request]
  (is (= (:uri request) "/items/1"))
  (is (= (:request-method request) :get))
  (is (= (:params request) {:id "1"})))

(defn items#create
  [request]
  (is (= (:uri request) "/items"))
  (is (= (:request-method request) :post)))

(defn items#update
  [request]
  (is (= (:uri request) "/items/1"))
  (is (= (:request-method request) :patch))
  (is (= (:params request) {:id "1"})))

(defn items#delete
  [request]
  (is (= (:uri request) "/items/1"))
  (is (= (:request-method request) :delete))
  (is (= (:params request) {:id "1"})))

(def routes
  (-> (GET    "/items"      items#index)
      (POST   "/items"      items#create)
      (GET    "/items/:id"  items#show)
      (PATCH  "/items/:id"  items#update)
      (DELETE "/items/:id"  items#delete)))

(deftest it-maps-routes-to-handlers
  (let [handler (route routes)]
    (handler (mock/request :get "/items"))
    (handler (mock/request :post "/items"))
    (handler (mock/request :get "/items/1"))
    (handler (mock/request :patch "/items/1"))
    (handler (mock/request :delete "/items/1"))))
