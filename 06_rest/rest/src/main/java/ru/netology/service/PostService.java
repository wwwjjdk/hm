package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class PostService {
  // сервис завязан на интерфейс, а не на конкретную реализацию
  private final PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<String> all() {
    List<String> array = new ArrayList<>();
    repository.all().forEach((k,y)->{
      if(!y.isRemoved()){
        array.add(y.getId()+" "+y.getContent());
      }
    });
    return array;
  }

  public Post getById(long id) {
    return repository.getById(id).orElseThrow(NotFoundException::new);
  }

  public Post save(Post post) {
    return repository.save(post);
  }

  public boolean removeById(long id) {
    return repository.removeById(id);
  }
}

