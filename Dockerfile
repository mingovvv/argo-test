# Dockerfile

# 사용할 베이스 이미지 (애플리케이션의 Java 버전에 맞춰 JRE 사용 권장)
FROM eclipse-temurin:21-jre-jammy

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사 (워크플로우에서 생성된 경로)
# 파일 이름이 버전 등을 포함할 수 있으므로 와일드카드 사용
COPY build/libs/*.jar app.jar

# 애플리케이션 포트 노출 (application.properties/yml 에서 설정한 포트)
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

# 선택사항: 타임존 설정 (필요시)
# ENV TZ=Asia/Seoul
# RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone