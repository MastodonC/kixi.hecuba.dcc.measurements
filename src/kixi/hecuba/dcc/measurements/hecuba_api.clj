(ns kixi.hecuba.dcc.measurements.hecuba-api
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [taoensso.timbre :as timbre]))

(defn get-entities [{:keys [endpoint username password]} property-code max-entries-per-page]
  (let [url-to-get (str endpoint
                        "4/entities/?q=property_code:"
                        property-code
                        "&page=0&size="
                        max-entries-per-page
                        "&sort_key=programme_name.lower_case_sort&sort_order=asc")]
    (timbre/infof "get url: %s" url-to-get)
    (try (-> (:body (http/get
                     url-to-get
                     {:basic-auth [username
                                   password]
                      :headers {"X-Api-Version" "2"}
                      :content-type :json
                      :socket-timeout 20000
                      :conn-timeout 20000}))
             (json/parse-string true)
             (get-in [:entities]))
         (catch Exception e (timbre/error e)))))

(defn post-measurements
  "Create the http post request for measurements
  uploads"
  [{:keys [endpoint username password]} json-payload entity-id device-id]
  (let [json-to-send (json/generate-string {:measurements json-payload})
        _ (timbre/info endpoint username password)
        endpoint (str endpoint "4/entities/" entity-id "/devices/" device-id "/measurements/")]
    (timbre/infof "post url: %s" endpoint)

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
         (finally {:message "post-measurements complete."}))))
