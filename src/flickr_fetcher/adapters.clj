(ns flickr-fetcher.adapters)

(defn feed-wire->internal
  [feed-wire]
  (some->> feed-wire
           :items
           (map (fn [{:keys [title media published]}]
                  {:title title
                   :media (:m media)
                   :published published}))))
