백엔드 개발환경
java 21
springboot 3.2.4

종속성
lombok
springboot devtool
spring web
spring security
querydsl

DB
spring data JPA
postgreSQL
H2

프론트엔드 개발환경
Node.js ( 20.12.1 )
Vue3
Nuxt3 ( 3.11.2 )
Vuetify3 ( 3.5.15)
Pinia ( 2.1.7 )
Pinia/nuxt ( 0.5.1 )
vee-validate

CREATE TABLE IF NOT EXISTS public."USER"
(
"USER_ID" BIGSERIAL NOT NULL,
"NAME" varchar(32) NOT NULL,
"EMAIL" varchar(32) NOT NULL UNIQUE,
"PASSWORD" varchar(128) NOT NULL,
"CREATED_AT" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
"UPDATED_AT" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT "USER_pkey" PRIMARY KEY ("USER_ID")
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."USER"
OWNER to postgres;

-- "UPDATED_AT" 칼럼을 자동 업데이트하기 위한 트리거 함수 생성
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
NEW."UPDATED_AT" = now();
RETURN NEW;
END;
$$ language 'plpgsql';

-- 테이블에 트리거 추가
CREATE TRIGGER update_user_modtime
BEFORE UPDATE ON public."USER"
FOR EACH ROW
EXECUTE PROCEDURE update_modified_column();

INSERT INTO "USER"("NAME", "EMAIL", "PASSWORD")
VALUES ('labol', 'labol@example.com', '{bcrypt}$2a$10$qsp2hyF8ed/12BSTWoKzkOmW/ibvjaqGPjCQ10Vllx3JdsWKyKUmu');

SELECT * FROM "USER";
