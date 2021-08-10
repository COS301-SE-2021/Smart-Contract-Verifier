create table public.user (public_walletid varchar(255) not null, alias varchar(255), nonce int4 not null, primary key (public_walletid))
create table agreements (contractid uuid not null, agreement_description varchar(255), agreement_imageurl varchar(255), agreement_title varchar(255), created_date timestamp, duration_conditionuuid uuid, moved_to_block_chain boolean not null, payment_conditionuuid uuid, sealed_date timestamp, blockchainid varchar(255), primary key (contractid))
create table conditions (conditionid uuid not null, condition_description varchar(255), condition_status bytea, condition_title varchar(255), proposal_date timestamp, contract_contractid uuid, proposing_user_public_walletid varchar(255), primary key (conditionid))
create table contact_list (contact_listid uuid not null, contact_list_name varchar(255), owner_public_walletid varchar(255), primary key (contact_listid))
create table contact_list_profile (profileid uuid not null, contact_alias varchar(255), contact_list_contact_listid uuid, user_public_walletid varchar(255), primary key (profileid))
create table evidence (evidence_hash varchar(255) not null, evidence_type bytea, contract_contractid uuid, evidence_url_evidenceid uuid, uploaded_url_evidenceid uuid, user_public_walletid varchar(255), primary key (evidence_hash))
create table linked_evidence (evidenceid uuid not null, evidence_url varchar(255), evidence_evidence_hash varchar(255), primary key (evidenceid))
create table messages (messageid uuid not null, message varchar(255), send_date timestamp, contract_contractid uuid, sender_public_walletid varchar(255), primary key (messageid))
create table message_status (message_statusid uuid not null, read_date timestamp, messagesid_messageid uuid, recipientid_public_walletid varchar(255), primary key (message_statusid))
create table uploaded_evidence (evidenceid uuid not null, file_mime_type varchar(255), filename varchar(255), evidence_evidence_hash varchar(255), primary key (evidenceid))
create table user_agreement (public_walletid varchar(255) not null, contractid uuid not null, primary key (public_walletid, contractid))
alter table conditions add constraint FKpr7yy9rgi6w2tupy14o3lpwur foreign key (contract_contractid) references agreements
alter table conditions add constraint FKf5jwtgem41ys5dqxul3mges72 foreign key (proposing_user_public_walletid) references public.user
alter table contact_list add constraint FK5d2wo4ku79ty5wy0sp91wmfne foreign key (owner_public_walletid) references public.user
alter table contact_list_profile add constraint FK1i7nmkgr287spuk0nbtuoq41a foreign key (contact_list_contact_listid) references contact_list
alter table contact_list_profile add constraint FKoexhgbgvmx752nsvk805144ov foreign key (user_public_walletid) references public.user
alter table evidence add constraint FKlpioqqatotxodcjc4rw9es0oc foreign key (contract_contractid) references agreements
alter table evidence add constraint FKajyqwe4nut3a9nlsnrg5jkdp6 foreign key (evidence_url_evidenceid) references linked_evidence
alter table evidence add constraint FKkeqst2mm0d8ikev3y3oc5ov6o foreign key (uploaded_url_evidenceid) references uploaded_evidence
alter table evidence add constraint FK98ib2d8e7lq8d96jwrup8u8qf foreign key (user_public_walletid) references public.user
alter table linked_evidence add constraint FKc3qd7g9wvw4gaxf16fuk3b47h foreign key (evidence_evidence_hash) references evidence
alter table messages add constraint FKtawvl3ujy864wa8yvej43o68b foreign key (contract_contractid) references agreements
alter table messages add constraint FKpogdllq7macivuimwivkatgaq foreign key (sender_public_walletid) references public.user
alter table message_status add constraint FK6stq2rkbviclmrtnucd2djw3n foreign key (messagesid_messageid) references messages
alter table message_status add constraint FKtc0v0t1qwbjtgohqvoeacsm0y foreign key (recipientid_public_walletid) references public.user
alter table uploaded_evidence add constraint FKi1tejij1ntihpua2py82nddoh foreign key (evidence_evidence_hash) references evidence
alter table user_agreement add constraint FKqmikxjshk09mrgx5fp6utjcmb foreign key (contractid) references agreements
alter table user_agreement add constraint FKm2k4vt46unbq35f9jckoxk12s foreign key (public_walletid) references public.user
