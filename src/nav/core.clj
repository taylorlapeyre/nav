(ns nav.core
  (:require [clout.core :as clout]))

(defn route
  [route-map]
  (fn handler [request]
    (let [route-strings   (map second (keys route-map))
          match-routes   #(hash-map :uri % :matches (clout/route-matches % request))
          analyzed-routes (map match-routes route-strings)
          matched-route   (first (filter :matches analyzed-routes))
          merged-params   (merge (:params request) (:matches matched-route))
          request         (assoc request :params merged-params)
          exact-handler   (get route-map [(:request-method request) (:uri matched-route)])]
      (if exact-handler
        (exact-handler request)
        ((or (:not-found route-map)
             (fn [req] {:status 404})) request)))))

(defn GET
  ([uri handler]
    (GET {} uri handler))
  ([routes-map uri handler]
    (assoc routes-map [:get uri] handler)))

(defn POST
  ([uri handler]
    (POST {} uri handler))
  ([routes-map uri handler]
    (assoc routes-map [:post uri] handler)))

(defn PATCH
  ([uri handler]
    (PATCH {} uri handler))
  ([routes-map uri handler]
    (assoc routes-map [:patch uri] handler)))

(defn PUT
  ([uri handler]
    (PUT {} uri handler))
  ([routes-map uri handler]
    (assoc routes-map [:put uri] handler)))

(defn DELETE
  ([uri handler]
    (DELETE {} uri handler))
  ([routes-map uri handler]
    (assoc routes-map [:delete uri] handler)))
