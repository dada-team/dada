INSERT INTO orchestrator(
	args, status)
SELECT substr(dd::text,1,10) as d , -1 FROM generate_series
( '2015-08-04'::timestamp 
	, now()
	, '1 day'::interval)as dd ;

INSERT INTO orchestrator(
	args, status)
SELECT substr(dd::text,1,10) as d , -1 FROM generate_series
( (SELECT max(args::timestamp) +'1 day' as max FROM orchestrator) 
	, now() +'1 day'
	, '1 day'::interval)as dd ;
