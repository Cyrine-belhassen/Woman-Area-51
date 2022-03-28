package tn.esprit.spring.womanarea51.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.womanarea51.entities.Post;
import tn.esprit.spring.womanarea51.entities.RatePub;
import tn.esprit.spring.womanarea51.entities.User;
import tn.esprit.spring.womanarea51.repositories.PostRepository;
import tn.esprit.spring.womanarea51.repositories.UserRepository;


@Service
public class PostServiceimpl implements IPostService{
@Autowired
PostRepository postRepository;
@Autowired
UserRepository userRepository;


public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";
	@Override
	public List<Post> getAllPosts() {
	//	 List<Post> posts = new ArrayList<>();
		   
		/*   postRepository.findAll()
		   .forEach(posts::add);
		   
		   return posts;*/
		 return postRepository.findAllByOrderByIdDesc();
	}

	@Override
	public Optional<Post> getPost(Long id) {
		return postRepository.findById(id);

	}
	@Override
	public List<Post> listepost(Long idUser) {
		User u = userRepository.findById(idUser).orElse(null);
		return postRepository.findByUserp(u);

}
	@Override
	public void addPost(Post post,Long idUser) {
		User u = userRepository.findById(idUser).orElse(null);
		
		if (post.getId()!=null) 
		{
				getById(post.getId());
				
		}

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
	
        post.setCreatedate(currentTimestamp);
		post.setUserp(u);
		
		postRepository.save(post);	
	}

	/*@Override
	public void updatePost(Long id, Post post) {
		User u =userRepository.findById(id).orElse(null);
		post.setTitle(post.getTitle());
		
		postRepository.save(post);
		
	}*/

	@Override
	public void deletePost(Long id) {
	     postRepository.deleteById(id);	
		
	}
	@Override
	public int getAllPostsForUser(Long idUser) {
		// TODO Auto-generated method stub
        User user = this.userRepository.findById(idUser).orElse(null);

        return this.postRepository.findAllByUserp(user).size();

	}

	@Override
	public void addPostt(Post post) {
		// TODO Auto-generated method stub
		postRepository.save(post);
	}

	@Override
	public void updatePost(Long id,Long idPost, Post post) {
		User u =userRepository.findById(id).orElse(null);
		Post p= postRepository.findById(idPost).orElse(null);
		p.setTitle(post.getTitle());
		p.setBody(post.getBody());
		
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		p.setCreatedate(currentTimestamp);
		postRepository.save(p);
		
	}
//liste post by token

	@Override
	public List<Post> listepostToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Post> searchpost(String w) {
		// TODO Auto-generated method stub
		List<Post> s=postRepository.findByTitleContaining(w);
		
		return s;
	}
	@Override
	public List<Post> rechTags(String s){
		List<Post> p = postRepository.retrieveByTag(s);
		return p;

}

	@Override
	public Post getById(Long id)throws NoSuchElementException {
		
			Optional<Post> publication = postRepository.findById(id);
			if (publication.isPresent()) return publication.get();
			throw new NoSuchElementException();
		}

	
	@Override
	public Post rate(Long idPost, Long idUser, RatePub rate){
	Post publication= getById(idPost);
	 RatePub userRate = publication.getRatingPub().get(idUser);
	 if (userRate != null) {
			if(userRate==RatePub.VERY_EXCELLENT){
					 publication.setScore(publication.getScore()-5);
			    }
			else if (userRate==RatePub.EXCELLENT) 
				{
					 publication.setScore((float) (publication.getScore()-4.5));
				 }
			 else if (userRate==RatePub.VERY_GOOD)  
				 {
					 publication.setScore((float) (publication.getScore()-4));
				 }
			 else if (userRate==RatePub.GOOD)  
				 {
					 publication.setScore((float) (publication.getScore()-3.5));
				 }
		    
		     else if (userRate==RatePub.ABOVE_AVERAGE) 
			      {
				     publication.setScore((float) (publication.getScore()-3));
			      }
	    	 else if (userRate==RatePub.AVERAGE) 
		    	  {  
		    		 publication.setScore((float) (publication.getScore()-2.5));
		          }
			 else if (userRate==RatePub.POOR) 
				   {
					  publication.setScore((float) (publication.getScore()-2));
		           }
			  else if (userRate==RatePub.VERY_POOR) 
				    {
					   publication.setScore((float) (publication.getScore()-1.5));
		            }
		      else if (userRate==RatePub.BAD) 
				    {
					    publication.setScore((float) (publication.getScore()-1));  
				    }
		      else 
				    {
					    publication.setScore((float) (publication.getScore()-0.5));
				    }
			 }
	 
	 if(rate==RatePub.VERY_EXCELLENT) 
		 { 
			 publication.setScore(publication.getScore()+5); 
		 }
	 else if (rate==RatePub.EXCELLENT) 
		{
			 publication.setScore((float) (publication.getScore()+4.5));
		 }
	 else if (rate==RatePub.VERY_GOOD)  
		 {
			 publication.setScore((float) (publication.getScore()+4));
		 }
	 else if (rate==RatePub.GOOD)  
		 {
			 publication.setScore((float) (publication.getScore()+3.5));
		 }
   
    else if (rate==RatePub.ABOVE_AVERAGE) 
	      {
		     publication.setScore((float) (publication.getScore()+3));
	      }
	 else if (rate==RatePub.AVERAGE) 
   	  {  
   		 publication.setScore((float) (publication.getScore()+2.5));
         }
	 else if (rate==RatePub.POOR) 
		   {
			  publication.setScore((float) (publication.getScore()+2));
          }
	  else if (rate==RatePub.VERY_POOR) 
		    {
			   publication.setScore((float) (publication.getScore()+1.5));
           }
     else if (rate==RatePub.BAD) 
		    {
			    publication.setScore((float) (publication.getScore()+1));  
		    }
     else 
		    {
			    publication.setScore((float) (publication.getScore()+0.5));
		    }
	  
	 publication.getRatingPub().put(idUser, rate);
	 publication = postRepository.save(publication);
		
		return publication;
}
	@Override
	public List<Post> postUnactiveComment() {
		List<Post> pubs = postRepository.affichNotNullPublication();
		///pubs.forEach(entity -> postRepository.delete(entity));
		return pubs;
	}

	@Override
	public void removeUnactiveComment() {
		List<Post> pubs = postRepository.affichNotNullPublication();
		pubs.forEach(entity ->{
			entity.setHistory(true);
			postRepository.save(entity);
		});
		//pubs.forEach(entity -> postRepository.delete(entity));
	}

	@Override
	public List<Post> getAllPostsByScore() {
		// TODO Auto-generated method stub
		return postRepository.findAllByOrderByScoreDesc();
	}

	@Override
	public Post maxScorePost() {
		
		return postRepository.findByScore();
	}

	private String uploadFile(MultipartFile uploadFile) {
		    String message = "";
		    try {
		     message= Paths.get(uploadDirectory, uploadFile.getOriginalFilename()).toString();
		    
		    } catch (Exception e) {
		      message = "Could not upload the file: " + uploadFile.getOriginalFilename() + "!";
		    }
		    return message;
		  }
	@Override
	public Post addPostImageTest( MultipartFile file, Long idPost){
		Post p = postRepository.findById(idPost).orElse(null);
		String url=uploadFile(file) ;
		p.setImage(url);
		return postRepository.save(p);
	}

	@Override
	public Post maxComPost() {
		
		return postRepository.findByCountCom();
	}
	
	}
	
