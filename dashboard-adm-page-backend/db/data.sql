--------------------------------------------------------
--  Arquivo criado - sexta-feira-outubro-03-2025   
--------------------------------------------------------
REM INSERTING into ALERTS
SET DEFINE OFF;
Insert into ALERTS (ALERT_ID,ALERT_TYPE,SEVERITY,CREATED_AT,REF_USER_ID,REF_POST_ID) values ('3','ENGAGEMENT_SPIKE','HIGH',to_date('26/09/25','DD/MM/RR'),null,null);
REM INSERTING into ENGAGEMENT_WEEKLY
SET DEFINE OFF;
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('1',to_date('19/05/25','DD/MM/RR'),to_date('25/05/25','DD/MM/RR'),'15','8','5');
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('2',to_date('26/05/25','DD/MM/RR'),to_date('01/06/25','DD/MM/RR'),'18','10','7');
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('3',to_date('02/06/25','DD/MM/RR'),to_date('08/06/25','DD/MM/RR'),'22','12','9');
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('4',to_date('09/06/25','DD/MM/RR'),to_date('15/06/25','DD/MM/RR'),'25','14','10');
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('5',to_date('16/06/25','DD/MM/RR'),to_date('22/06/25','DD/MM/RR'),'28','16','12');
Insert into ENGAGEMENT_WEEKLY (WEEK_ID,START_DATE,END_DATE,TOTAL_LIKES,TOTAL_COMMENTS,TOTAL_SHARES) values ('6',to_date('23/06/25','DD/MM/RR'),to_date('29/06/25','DD/MM/RR'),'30','18','14');
REM INSERTING into INTERACTIONS
SET DEFINE OFF;
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('25','12','3','LIKE',to_date('26/09/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('26','12','3','LIKE',to_date('26/09/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('31','13','4','LIKE',to_date('26/09/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('1','1','2','LIKE',to_date('11/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('2','1','3','COMMENT',to_date('11/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('3','2','1','LIKE',to_date('12/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('4','2','4','SHARE',to_date('12/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('5','3','5','LIKE',to_date('13/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('6','4','2','COMMENT',to_date('14/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('7','5','3','SHARE',to_date('15/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('8','6','4','LIKE',to_date('16/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('9','7','5','COMMENT',to_date('17/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('10','8','1','SHARE',to_date('18/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('11','9','2','LIKE',to_date('19/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('12','10','3','COMMENT',to_date('20/06/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('13','3','2','LIKE',to_date('26/09/25','DD/MM/RR'));
Insert into INTERACTIONS (INTERACTION_ID,POST_ID,USER_ID,TYPE,CREATED_AT) values ('14','1','9','LIKE',to_date('26/09/25','DD/MM/RR'));
REM INSERTING into POSTS
SET DEFINE OFF;
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('11','3','sdgsfdgasdg',to_date('26/09/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('12','3','sdgsfdgasdg',to_date('26/09/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('1','1','Primeiro post',to_date('01/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('2','2','Segundo post',to_date('02/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('3','3','Terceiro post',to_date('03/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('4','4','Quarto post',to_date('04/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('5','5','Quinto post',to_date('05/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('6','1','Sexto post',to_date('06/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('7','2','Sétimo post',to_date('07/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('8','3','Oitavo post',to_date('08/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('9','4','Nono post',to_date('09/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('10','5','Décimo post',to_date('10/06/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('13','6','string',to_date('26/09/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('14','5','string',to_date('26/09/25','DD/MM/RR'));
Insert into POSTS (POST_ID,USER_ID,TITLE,CREATED_AT) values ('15','15','string',to_date('03/10/25','DD/MM/RR'));
REM INSERTING into ROLES
SET DEFINE OFF;
Insert into ROLES (ID,NAME) values ('1','USER');
Insert into ROLES (ID,NAME) values ('2','ADMIN');
REM INSERTING into USER_GROWTH_MONTHLY
SET DEFINE OFF;
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-01','2');
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-02','3');
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-03','4');
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-04','5');
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-05','5');
Insert into USER_GROWTH_MONTHLY (MONTH_YEAR,TOTAL_USERS) values ('2025-06','5');
REM INSERTING into USER_ROLES
SET DEFINE OFF;
Insert into USER_ROLES (USER_ID,ROLE_ID) values ('1','1');
Insert into USER_ROLES (USER_ID,ROLE_ID) values ('2','2');
REM INSERTING into USERS
SET DEFINE OFF;
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('1','Alice','alice@email.com',to_date('01/03/25','DD/MM/RR'),'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('2','Bob','bob@email.com',to_date('05/03/25','DD/MM/RR'),'$2a$10$Q9w1QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2QK2');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('3','Carol','carol@email.com',to_date('10/04/25','DD/MM/RR'),'$2a$10$Q9w1QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3QK3');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('4','David','david@email.com',to_date('15/05/25','DD/MM/RR'),'$2a$10$Q9w1QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4QK4');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('5','Eva','eva@email.com',to_date('20/06/25','DD/MM/RR'),'$2a$10$Q9w1QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5QK5');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('6','Frank','frank@email.com',to_date('22/06/25','DD/MM/RR'),'$2a$10$F1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('7','Grace','grace@email.com',to_date('23/06/25','DD/MM/RR'),'$2a$10$G1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('8','Henry','henry@email.com',to_date('24/06/25','DD/MM/RR'),'$2a$10$H1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('9','Irene','irene@email.com',to_date('25/06/25','DD/MM/RR'),'$2a$10$I1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('10','Jack','jack@email.com',to_date('26/06/25','DD/MM/RR'),'$2a$10$J1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('11','Karen','karen@email.com',to_date('27/06/25','DD/MM/RR'),'$2a$10$K1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('12','Leo','leo@email.com',to_date('28/06/25','DD/MM/RR'),'$2a$10$L1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('13','Mia','mia@email.com',to_date('29/06/25','DD/MM/RR'),'$2a$10$M1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('14','Noah','noah@email.com',to_date('30/06/25','DD/MM/RR'),'$2a$10$N1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('15','Olivia','olivia@email.com',to_date('01/07/25','DD/MM/RR'),'$2a$10$O1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('16','Peter','peter@email.com',to_date('02/07/25','DD/MM/RR'),'$2a$10$P1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('17','Quinn','quinn@email.com',to_date('03/07/25','DD/MM/RR'),'$2a$10$Q1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('18','Ryan','ryan@email.com',to_date('04/07/25','DD/MM/RR'),'$2a$10$R1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('19','Sara','sara@email.com',to_date('05/07/25','DD/MM/RR'),'$2a$10$S1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('20','Tom','tom@email.com',to_date('06/07/25','DD/MM/RR'),'$2a$10$T1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('21','Uma','uma@email.com',to_date('07/07/25','DD/MM/RR'),'$2a$10$U1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('22','Victor','victor@email.com',to_date('08/07/25','DD/MM/RR'),'$2a$10$V1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
Insert into USERS (USER_ID,USER_NAME,EMAIL,CREATED_AT,USER_PASSWORD) values ('23','Wendy','wendy@email.com',to_date('09/07/25','DD/MM/RR'),'$2a$10$W1aBcDeFgHiJkLmNoPqrsu1234567890abcdEFGHIJKLMNOPQRST');
