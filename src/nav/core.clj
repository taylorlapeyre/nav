(ns nav.core
  (:require [clout.core :as clout]))

(defn routes->handler
  [route-map]
  (fn handler [request]
    (let [route-strings   (map second (keys route-map))
          route->matches #(hash-map :uri % :matches (clout/route-matches % request))
          analyzed-routes (map route->matches route-strings)
          matched-route   (first (filter :matches analyzed-routes))
          merged-params   (merge (:params request) (:matches matched-route))
          request         (assoc request :params merged-params)
          exact-handler   (get route-map [(:request-method request) (:uri matched-route)])]
      (if exact-handler
        (exact-handler request)
        ((or (:not-found route-map)
             (fn [req] {:status 404})) request)))))

(defn GET [routes-map uri handler]
  (assoc routes-map [:get uri] handler))

(defn POST [routes-map uri handler]
  (assoc routes-map [:post uri] handler))

(defn PATCH [routes-map uri handler]
  (assoc routes-map [:patch uri] handler))

(defn PUT [routes-map uri handler]
  (assoc routes-map [:put uri] handler))

(defn DELETE [routes-map uri handler]
  (assoc routes-map [:delete uri] handler))
