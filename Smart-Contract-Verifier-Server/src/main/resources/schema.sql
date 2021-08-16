create table if not exists agreements (contractid uuid not null, created_date timestamp, duration int8, moved_to_block_chain boolean not null, partya varchar(255), partyb varchar(255), sealed_date timestamp, blockchainid varchar(255), primary key (contractid));
create table if not exists agreements_conditions (agreements_contractid uuid not null, conditions_conditionid uuid not null);
create table if not exists agreements_messages (agreements_contractid uuid not null, messages_messageid uuid not null);
create table if not exists conditions (conditionid uuid not null, condition_description varchar(255), condition_status bytea, proposal_date timestamp, proposing_user varchar(255), contract_contractid uuid, primary key (conditionid));
create table if not exists contact_list (contact_listid uuid not null, contact_list_name varchar(255), ownerid varchar(255), primary key (contact_listid));
create table if not exists evidence (evidence_hash varchar(255) not null, evidence_type bytea, contract_contractid uuid, evidence_url_evidenceid uuid, uploaded_url_evidenceid uuid, user_public_walletid varchar(255), primary key (evidence_hash));
create table if not exists linked_evidence (evidenceid uuid not null, evidence_url varchar(255), evidence_evidence_hash varchar(255), primary key (evidenceid));
create table if not exists messages (messageid uuid not null, message varchar(255), send_date timestamp, contract_contractid uuid, sender_public_walletid varchar(255), primary key (messageid));
create table if not exists message_status (message_statusid uuid not null, read_date timestamp, messagesid_messageid uuid, recipientid_public_walletid varchar(255), primary key (message_statusid));
create table if not exists uploaded_evidence (evidenceid uuid not null, file_mime_type varchar(255), filename varchar(255), evidence_evidence_hash varchar(255), primary key (evidenceid));
create table if not exists "user" (public_walletid varchar(255) not null, alias varchar(255), email varchar(255), primary key (public_walletid));
create table if not exists user_agreements (user_public_walletid varchar(255) not null, agreements_contractid uuid not null);
alter table if exists agreements_conditions add constraint UK_at0qick1msk18s9hsputshyyw unique (conditions_conditionid);
alter table if exists agreements_messages add constraint UK_afgb9sc48v1gotdmowm37qupl unique (messages_messageid);
alter table if exists user_agreements add constraint UK_svlb2p4l2rqs5pv5r9gybcxj unique (agreements_contractid);
alter table if exists agreements_conditions add constraint FKhf094sapg5c1q0yvaw0ufbn66 foreign key (conditions_conditionid) references conditions;
alter table if exists agreements_conditions add constraint FKpi3wqi2xmhofcem0xcga2daff foreign key (agreements_contractid) references agreements;
alter table if exists agreements_messages add constraint FK29002yv87adacnm8ruduqjt48 foreign key (messages_messageid) references messages;
alter table if exists agreements_messages add constraint FKrnxrafybfiwwtkhixg7icp7ad foreign key (agreements_contractid) references agreements;
alter table if exists conditions add constraint FKpr7yy9rgi6w2tupy14o3lpwur foreign key (contract_contractid) references agreements;
alter table if exists evidence add constraint FKlpioqqatotxodcjc4rw9es0oc foreign key (contract_contractid) references agreements;
alter table if exists evidence add constraint FKajyqwe4nut3a9nlsnrg5jkdp6 foreign key (evidence_url_evidenceid) references linked_evidence;
alter table if exists evidence add constraint FKkeqst2mm0d8ikev3y3oc5ov6o foreign key (uploaded_url_evidenceid) references uploaded_evidence;
alter table if exists evidence add constraint FK98ib2d8e7lq8d96jwrup8u8qf foreign key (user_public_walletid) references "user";
alter table if exists linked_evidence add constraint FKc3qd7g9wvw4gaxf16fuk3b47h foreign key (evidence_evidence_hash) references evidence;
alter table if exists messages add constraint FKtawvl3ujy864wa8yvej43o68b foreign key (contract_contractid) references agreements;
alter table if exists messages add constraint FKpogdllq7macivuimwivkatgaq foreign key (sender_public_walletid) references "user";
alter table if exists message_status add constraint FK6stq2rkbviclmrtnucd2djw3n foreign key (messagesid_messageid) references messages;
alter table if exists message_status add constraint FKtc0v0t1qwbjtgohqvoeacsm0y foreign key (recipientid_public_walletid) references "user";
alter table if exists uploaded_evidence add constraint FKi1tejij1ntihpua2py82nddoh foreign key (evidence_evidence_hash) references evidence;
alter table if exists user_agreements add constraint FKcai91i3f62212ubvr2ovtee3s foreign key (agreements_contractid) references agreements;
alter table if exists user_agreements add constraint FKd54k8p4t3ln8cl0of3vay2naj foreign key (user_public_walletid) references "user";
