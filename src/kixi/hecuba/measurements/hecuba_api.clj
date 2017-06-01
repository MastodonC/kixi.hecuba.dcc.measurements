(ns kixi.hecuba.measurements.hecuba-api
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [taoensso.timbre :as timbre]))

(defn post-measurements
  "Create the http post request for measurements
  uploads"
  [{:keys [endpoint username password]} json-payload entity-id device-id]
  (let [json-to-send (json/generate-string {:measurements json-payload})
        _ (timbre/info endpoint username password)
        endpoint (str endpoint "entities/" entity-id "/devices/" device-id "/measurements/")]
    (timbre/infof "Using endpoint: %s" endpoint)

    (try (http/post
          endpoint
          {:basic-auth [username password]
           :body json-to-send
           :headers {"X-Api-Version" "2"}
           :content-type :json
           :socket-timeout 20000
           :conn-timeout 20000
           :accept "application/json"})
         (catch Exception e (doall (str "Caught Exception " (.getMessage e))
                                   (timbre/error e "> There was an error during the upload to entity " entity-id)))
         (finally {:message "push-payload-to-hecuba complete."}))))
