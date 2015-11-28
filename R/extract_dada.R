options(warn=-1)
library(RPostgres)
library(DBI)
library(data.table)
library(paRisni)
#
# Connect to the database
#
chunk2 <- function(x,n) split(x, cut(seq_along(x), n, labels = FALSE))
dbname <- "dadabase"
user <- "postgres"
password <- gsub("^.*:","", scan("~/.pgpass",what="")[1])
host <- "localhost"
con <- dbConnect(RPostgres::Postgres(), user=user,
		   password=password, dbname=dbname, host=host)

ddr <- data.table(dbGetQuery(con,"SELECT * FROM dadarace"))
setkey(ddr,"id_race")
ddr[,race_city:=gsub("-prix.*$","",race)]
ddr[,race_prix:=gsub("^.*-prix-","",race)]
ddr[,race_hour:=gsub("^.*d.part vers ","",description)]
ddr[,race_distance:=gsub("[[:punct:]]","",unlist(strExtractAll("\\b((?:\\d+)|(?:\\d+[.]\\d+))[ ]*(?:m)",description,capture=T)))]
ddr[,race_type:=unlist(strExtractAll("\\b((?:Attel)|(?:Mont))",description,capture=T))]
ddr[,race_haies:=grepl("haies",description,ignore.case=T)]
ddr[,race_attel:=grepl("attel",description,ignore.case=T)]
ddr[,race_poly:=grepl("polytrack",description,ignore.case=T)]
ddr[,race_monte:=grepl("mont",description,ignore.case=T)]
ddr[,race_steeple:=grepl("Steeple",description,ignore.case=T)]
ddr[,race_plat:=grepl("Plat",description,ignore.case=T)]
ddr[,race_trot_monte:=grepl("monté",description,ignore.case=T)]
ddr[,race_cross:=grepl("cross",description,ignore.case=T)]
ddr[,race_steeple_chase:=grepl("chase",description,ignore.case=T)]
ddr[,race_price_value:=unlist(strExtractAll("^\\b((?:\\d+)|(?:\\d+[.]\\d+)) -",description,capture=T))]
ddr[,race_sex:=ifelse(grepl("femelle",description,ignore.case=T),"f",ifelse(grepl("m[aâä]le",description,ignore.case=T),"m","b"))]
ddr[,race_handicap:=unlist(strExtractAll("- (handicap \\w*?) -",description,capture=T))]
ddr[,description:=NULL]
ddr[,race:=NULL]
ddr[,race_type_bis:=ifelse(race_steeple_chase,"steeple_chase",ifelse(race_cross,"cross",ifelse(race_trot_monte,"trot_monte",ifelse(race_haies,"haies",ifelse(race_steeple,"steeple",ifelse(race_plat,"plat",ifelse(race_attel,"attelé",ifelse(race_monte,"monté",ifelse(race_poly,"polytrack","inconnu")))))))))]
ddr[,c("race_type","race_haies","race_attel","race_poly","race_monte","race_steeple","race_plat","race_trot_monte","race_cross","race_steeple_chase"):=NULL]
setnames(ddr,"race_type_bis","race_type")


ddh <- data.table(dbGetQuery(con,"SELECT distinct * FROM dadahorse"))
setkey(ddh,"id_race")
dd <- ddh[ddr]
dd[,race_nb_horse:=.N,by=id_race]
dd[,horse_age:=as.numeric(gsub("[^0-9]","",horse_age))]
rm(ddr)
rm(ddh)

ddp <- data.table(dbGetQuery(con,"SELECT * FROM dadapronostic"))
setkey(ddp,"id_race","horse")
setkey(dd,"id_race","horse_name")
dd <- ddp[dd]
dd[,horse_race_count:=.N,by=horse]
rm(ddp)
gc()
setnames(dd,"place","pronostic_place")
setnames(dd,"i.place","place")
setnames(dd,"horse","horse_name")

dd[,pronostic_place:=as.numeric(pronostic_place)]
dd[,place:=as.numeric(place)]
dd[place!=-1,place:=place+1]
dd[place!=-1,pronostic_expected:=pronostic_place/place]
dd[,date:=as.Date(date,format="%Y-%m-%d")]
setkey(dd,"horse_name")
dd <- dd[dd,allow.cartesian=T][date>=i.date]
dd[,horse_mean_overall:=mean(1/i.place[date!=i.date]),by=list(horse_name,race_prix,date)]
dd[,horse_mean_overall_race_type:=mean(1/i.place[date!=i.date&race_type==i.race_type]),by=list(horse_name,race_prix,date)]
dd[,horse_mean_overall_race_price_value:=mean(1/i.place[date!=i.date&race_price_value==i.race_price_value]),by=list(horse_name,race_prix,date)]
dd[,horse_mean_overall_race_city:=mean(1/i.place[date!=i.date&race_city==i.race_city]),by=list(horse_name,race_prix,date)]
dd[,horse_mean_overall_race_age:=mean(1/i.place[date!=i.date&horse_age==i.horse_age]),by=list(horse_name,race_prix,date)]
dd <- dd[!duplicated(paste0(horse_name,race_prix,date)),list(id_race,pronostiqueur,horse_name,pronostic_place,horse_age,horse_sex,corde,oeillere,record,distance,horse_last_result,place,cote,jockey,jockey_poids,trainer,date,race_city,race_prix,race_hour,race_distance,race_price_value,race_sex,race_handicap,race_type,race_nb_horse,horse_race_count,pronostic_expected,horse_mean_overall,horse_mean_overall_race_type,horse_mean_overall_race_price_value,horse_mean_overall_race_city,horse_mean_overall_race_age)]
#tests
#dd[,horse_mean_overall:=mean(1/place[place!=-1]),by=horse_name]
#dd[,horse_mean_overall_race_type:=mean(1/place[place!=-1]),by=list(horse_name,race_type)]
#dd[,horse_mean_overall_race_price_value:=mean(1/place[place!=-1]),by=list(horse_name,race_price_value)]
#dd[,horse_mean_overall_race_city:=mean(1/place[place!=-1]),by=list(horse_name,race_city)]
#dd[,horse_mean_overall_race_age:=mean(1/place[place!=-1]),by=list(horse_name,race_city)]

#load("dd")
#dt[,id_jockey:=as.factor(jockey_name)]
#dt[,id_jockey:=as.numeric(id_jockey)]
#dt <- dt[,list(id_jockey,horse_name,race_prix,date,race_type,race_price_value,race_city,horse_age,jockey_name)]
#head(dt[,list(id_jockey,jockey_name)])
#dbWriteTable(con,"dt",dt,append=T)
#tables()
##ddr <- data.table(dbGetQuery(con,"SELECT * FROM dt dt1 JOIN dt dt2 USING (id_jockey) WHERE dt1.date >= dt2.date"))

setnames(dd,"jockey","jockey_name")
dd[,jockey_name:=as.factor(jockey_name)]
setkey(dd,"horse_name","race_prix","jockey_name","date")
dt <- copy(dd[,list(horse_name,race_prix,date,place,race_type,race_price_value,race_city,horse_age,jockey_name)])
save(dd,file="dd"); rm(dd); gc()
setkey(dt,"jockey_name")
#dt <- dt[dt,allow.cartesian=T][date>=i.date]
jocks <- unique(dt$jockey_name)
i <- 0
dt3 <- data.table()
for(jock in chunk2(1:length(jocks),150)){
i<-i+1;	
print(paste0(min(jock),"_",max(jock)))
print(i)
jock <- jocks[min(jock):max(jock)]
dt2 <- dt[J(jock),]
dt2 <- dt2[dt2,allow.cartesian=T][date>=i.date]
dt2[,jockey_mean_overall:=mean(1/i.place[date!=i.date]),by=list(jockey_name,race_prix,date)]
dt2[,jockey_mean_overall_race_type:=mean(1/i.place[date!=i.date&race_type==i.race_type]),by=list(jockey_name,race_prix,date)]
dt2[,jockey_mean_overall_race_price_value:=mean(1/i.place[date!=i.date&race_price_value==i.race_price_value]),by=list(jockey_name,race_prix,date)]
dt2[,jockey_mean_overall_race_city:=mean(1/i.place[date!=i.date&race_city==i.race_city]),by=list(jockey_name,race_prix,date)]
dt2[,jockey_mean_overall_race_age:=mean(1/i.place[date!=i.date&horse_age==i.horse_age]),by=list(jockey_name,race_prix,date)]
dt2 <- dt2[!duplicated(paste0(horse_name,race_prix,jockey_name,date)),list(horse_name,race_prix,jockey_name,date,jockey_mean_overall,jockey_mean_overall_race_type,jockey_mean_overall_race_price_value,jockey_mean_overall_race_city,jockey_mean_overall_race_age)] 
dt3 <- rbindlist(list(dt3,dt2))
}
save(dt3,file="dt2"); rm(dt3); gc()

#tests
#dd[,jockey_mean_overall:=mean(1/place[place!=-1]),by=jockey_name]
#dd[,jockey_mean_overall_race_type:=mean(1/place[place!=-1]),by=list(jockey_name,race_type)]
#dd[,jockey_mean_overall_race_price_value:=mean(1/place[place!=-1]),by=list(jockey_name,race_price_value)]
#dd[,jockey_mean_overall_race_city:=mean(1/place[place!=-1]),by=list(jockey_name,race_city)]
#dd[,jockey_mean_overall_race_age:=mean(1/place[place!=-1]),by=list(jockey_name,race_city)]

load("dd")
setnames(dd,"trainer","trainer_name")
dt <- copy(dd[,list(horse_name,race_prix,date,place,race_type,race_price_value,race_city,horse_age,trainer_name)])
save(dd,file="dd"); rm(dd); gc()
setkey(dt,"trainer_name")
jocks <- unique(dt$trainer_name)
i <- 0
dt3 <- data.table()
for(jock in chunk2(1:length(jocks),200)){
i<-i+1;	
print(paste0(min(jock),"_",max(jock)))
print(i)
jock <- jocks[min(jock):max(jock)]
dt2 <- dt[J(jock),]
dt2 <- dt2[dt2,allow.cartesian=T][date>=i.date]
dt2[,trainer_mean_overall:=mean(1/i.place[date!=i.date]),by=list(trainer_name,race_prix,date)]
dt2[,trainer_mean_overall_race_type:=mean(1/i.place[date!=i.date&race_type==i.race_type]),by=list(trainer_name,race_prix,date)]
dt2[,trainer_mean_overall_race_price_value:=mean(1/i.place[date!=i.date&race_price_value==i.race_price_value]),by=list(trainer_name,race_prix,date)]
dt2[,trainer_mean_overall_race_city:=mean(1/i.place[date!=i.date&race_city==i.race_city]),by=list(trainer_name,race_prix,date)]
dt2[,trainer_mean_overall_race_age:=mean(1/i.place[date!=i.date&horse_age==i.horse_age]),by=list(trainer_name,race_prix,date)]
dt2 <- dt2[!duplicated(paste0(horse_name,race_prix,trainer_name,date)),list(horse_name,race_prix,trainer_name,date,trainer_mean_overall,trainer_mean_overall_race_type,trainer_mean_overall_race_price_value,trainer_mean_overall_race_city,trainer_mean_overall_race_age)] 
setkey(dt2,"horse_name","race_prix","trainer_name","date")
dt3 <- rbindlist(list(dt3,dt2))
}
save(dt3,file="dt3"); rm(dt3); gc()


load("dd")
load("dt2")
setkey(dd,"horse_name","race_prix","jockey_name","date")
setkey(dt2,"horse_name","race_prix","jockey_name","date")
dd <- dt2[dd]
rm(dt2);gc();

load("dt3")
setkey(dd,"horse_name","race_prix","trainer_name","date")
setkey(dt3,"horse_name","race_prix","trainer_name","date")
dd <- dt3[dd]
rm(dt2);gc();


#tests
#dd[,trainer_mean_overall:=mean(1/place[place!=-1]),by=trainer_name]
#dd[,trainer_mean_overall_race_type:=mean(1/place[place!=-1]),by=list(trainer_name,race_type)]
#dd[,trainer_mean_overall_race_price_value:=mean(1/place[place!=-1]),by=list(trainer_name,race_price_value)]
#dd[,trainer_mean_overall_race_city:=mean(1/place[place!=-1]),by=list(trainer_name,race_city)]
#dd[,trainer_mean_overall_race_age:=mean(1/place[place!=-1]),by=list(trainer_name,race_city)]

dd[,horse_last_result:=NULL]
dd <- dd[][order(id_race,place,decreasing=T)]
dd <- dd[,list(id_race,pronostiqueur,horse_name,pronostic_place,horse_age,horse_sex,corde,oeillere,record,distance,place,cote,jockey_name,jockey_poids,trainer_name,date,race_city,race_prix,race_hour,race_distance,race_price_value,race_sex,race_handicap,race_type,race_nb_horse,horse_race_count,pronostic_expected,horse_mean_overall,horse_mean_overall_race_type,horse_mean_overall_race_price_value,horse_mean_overall_race_city,horse_mean_overall_race_age,jockey_mean_overall,jockey_mean_overall_race_type,jockey_mean_overall_race_price_value,jockey_mean_overall_race_city,jockey_mean_overall_race_age,trainer_mean_overall,trainer_mean_overall_race_type,trainer_mean_overall_race_price_value,trainer_mean_overall_race_city,trainer_mean_overall_race_age)]
#dd <- dd[as.Date(date,format="%Y-%m-%d")>as.Date("2015-11-20",format="%Y-%m-%d"),]
write.table(dd,"~/20151127_dadabase.csv",na="",sep=",",dec=".",row.names=F,col.names=T,quote=T)
