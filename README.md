# Kotlin With React でゲーム制作　サンプルプロジェクト

こちらは技術書典3で頒布されたサークルStrayCalicoのmikenoko magagine vol.2の Kotlin with React でゲーム制作の章のサンプルプロジェクトです。

こちらのプロジェクトでは
https://github.com/Kotlin/kotlin-fullstack-sample
のfrontend/src/org/jetbrains/{common, react}のソースコードがないと動作いたしません。
これらのソースコードを手動で本プロジェクトのkotlin-game-sample/kotlin-game-frontend/src/main/kotlin以下に配置してください。(kotlinはソースのパッケージ名とディレクトリ構造が一致している必要はないので、ソースとして読み込まれる場所ならどこにおいても動くはずです)
これらのソースコードのライセンスが不透明であるため、このような配布体系を取らせていただきました。
jetbrainsとしては"You’re welcome to use the wrappers in your project and adapt them to your own needs."と発言しているのですが、安全のためこのようにしています。(https://blog.jetbrains.com/kotlin/2017/04/use-kotlin-with-npm-webpack-and-react/)

触ってみたい人は以下を参考に起動してください

```
git clone https://github.com/bzm22613/kotlin-game-sample
cd kotlin-game-sample
jetbrainsのreactのコードを手動で配置
```

#### フロントエンドの起動
```
./gradlew kotlin-game-frontend:bundle
./gradlew kotlin-game-frontend:run
```
localhost:3000でフロントエンドが起動します。

#### バックエンドの起動
```
./gradlew kotlin-game-backend:bootRun
```
localhost:8080でバックエンドが起動します。