package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
  private ConcurrentHashMap<Long,Post> map = new ConcurrentHashMap<>();
  private AtomicLong atomicLong = new AtomicLong();
  public ConcurrentHashMap<Long,Post> all() {
    return map;
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

  public boolean removeById(long id) {
    if(map.containsKey(id)){
      map.get(id).setRemoved(true);
      return true;
    }else{
      return false;
    }
  }
}