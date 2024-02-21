# ビルドステージ
FROM maven:3-eclipse-temurin-21 AS build
COPY . .
# Mavenを使ってパッケージをビルド
RUN mvn clean package -Dmaven.test.skip=true
# JARファイルを展開
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# 実行ステージ
FROM eclipse-temurin:21-alpine
VOLUME /tmp
# ビルドステージからファイルをコピー
ARG DEPENDENCY=target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-Dspring.backgroundpreinitializer.ignore=true", "-cp", "app:app/lib/*", "com.nyakako.simplesave.SimplesaveApplication"]
# ↓JVMオプションを適用(速度低下の為、中止)
# ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-Dspring.backgroundpreinitializer.ignore=true", "-cp", "app:app/lib/*", "com.nyakako.simplesave.SimplesaveApplication"]