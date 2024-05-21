delimiter $$

create view CategoryInfo as
select t.title as `title`, count(p.id) as `publications`
from publication_tag t
inner join publication_tags p_t on p_t.tag_id = t.id
inner join publication p on p_t.publication_id = p.id
group by t.title$$

delimiter ;