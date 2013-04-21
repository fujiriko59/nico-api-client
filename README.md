#nico-api-client
ニコニコ動画APIのJavaライブラリ。
##build
    $ git clone git@github.com:fujiriko59/nico-api-client.git
    $ cd nico-api-client
    $ mvn clean package

##samples
*ログイン*  
ほとんどのAPIの実行にはログインが必要。しなくてもいいのもある。

    NiconicoApiClient client = new NiconicoApiClient();
    client.login("mail address", "password");

*動画検索*

    List<SearchResult> results = client.search("らき☆すた", "m", 1, "d", true);

*コメント取得*

    List<CommentInfo> list = client.getComment("sm14027065");

*ランキング取得*

    List<RankingInfo> list = client.getRanking("daily", "fav");

*マイリスト取得*  
ログインしているアカウントのマイリストを取得。

    List<Mylist> list = client.getOwnerMylists();
    List<MylistItem> items = client.getMylistItems(list.get(0).id);

*動画ダウンロード*

    client.downloadVideo("sm15039386", ".");

