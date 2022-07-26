package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
  private ConcurrentHashMap<Long,Post> map = new ConcurrentHashMap<>();
  private AtomicLong atomicLong = new AtomicLong();
  public List<Post> all() {
    return new ArrayList<>(map.values());
  }

  public Optional<Post> getById(long id) {
    if(map.containsKey(id)){
       if(!map.get(id).isRemoved()){
         return Optional.of(map.get(id));
       }else{
         return Optional.empty();
       }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if(post.getId()==0){
      long atomicId = atomicLong.incrementAndGet();
      post.setId(atomicId);
      map.put(post.getId(), post);
    }else if(post.getId()!=0){
      if(map.containsKey(post.getId())){
        if(!map.get(post.getId()).isRemoved()){
          map.remove(post.getId());
          map.put(post.getId(), post);
        }else{
          throw  new NotFoundException();
        }
      }else{
        throw new NotFoundException("Post not saved {id:" + post.getId() + "}");
      }
    }
    return post;
  }

  public void removeById(long id) {
    if(map.containsKey(id) ){
      if(!map.get(id).isRemoved()){
        map.get(id).setRemoved(true);
      }else{
        throw new NotFoundException("Объект уже удален");
      }
    }else{
      throw  new NotFoundException();
    }
  }
}