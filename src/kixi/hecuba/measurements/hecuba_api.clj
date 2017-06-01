(ns kixi.hecuba.measurements.hecuba-api
  (:require  [clj-http.client :as http]
             [cheshire.core :as json]
             [environ.core :refer [env]]
             [taoensso.timbre :as timbre]))

(defn push-payload-to-hecuba
  "Create the http post request for measurements
  uploads"
  [json-payload entity-id device-id]
  (let [json-to-send (json/generate-string {:measurements json-payload})
        endpoint (str (env :hecuba-endpoint) "entities/" entity-id "/devices/" device-id "/measurements/")]
    (timbre/infof "Using endpoint: %s" endpoint)

    (try (http/post
          endpoint
          {:basic-auth [(env :hecuba-username) (env :hecuba-password)]
           :body json-to-send
           :headers {"X-Api-Version" "2"}
           :content-type :json
           :socket-timeout 20000
           :conn-timeout 20000
           :accept "application/json"})
         (catch Exception e (doall (str "Caught Exception " (.getMessage e))
                                   (timbre/error e "> There was an error during the upload to entity " entity-id)))
         (finally {:message "push-payload-to-hecuba complete."}))))
