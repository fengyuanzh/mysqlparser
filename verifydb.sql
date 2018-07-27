# If the result is not empty then the cofiguration is incorrect

# For each "response" or "other_key" in table sp_metafields we should have one row in table sp_metaresponses
select *  
from sp_metafields  mf 
left join  sp_metaresponses  mr 
on (mf.datasetInfoID = mr.datasetInfoId and mf.fieldName = mr.fieldName   and mf.fieldTypeBehaviour = mr.fieldTypeBehaviour and mf.fieldType = mr.fieldType )
where mf.fieldType in ("response","other_key"  ) 
and mr.datasetInfoID is null;


# For each ratio calculation(3 rows) in table sp_metatable we should have one row in table sp_metaresponses
select * 
from sp_metatable mt  
left join  sp_metaresponses mr
on (mt.datasetInfoID = mr.datasetInfoId and mt.name = mr.fieldName )
where mr.datasetInfoID is null;


# For each "time" in table sp_metafields we should have one row in table sp_metatime
select * 
from sp_metafields mf  
left join  sp_metatime mt 
on (mf.datasetInfoID = mt.datasetInfoId and mf.fieldName = mt.time ) 
where mf.fieldType = "time"
and mt.datasetInfoID is null;
