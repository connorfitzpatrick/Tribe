import { Component, OnInit, Input, Renderer2 } from '@angular/core';
import { PostService } from '../../services/PostService';
import { User } from '../../models/profile.model';
import { AuthService } from '../../services/AuthService';
import { ImageService } from '../../services/ImageService';
import { AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css'],
})
export class FeedComponent implements OnInit {
  private postsSubscription?: Subscription;
  @Input() userId: number | undefined;
  @Input() user: User | undefined;
  posts: any[] = [];
  userPic: string | null = null;

  isCurrentUserProfile: boolean = false;
  @Input() isProfilePage: boolean = false;
  currentPage: number = 0;

  constructor(
    public postService: PostService,
    private authService: AuthService,
    private router: Router,
    private imageService: ImageService
  ) {}

  async ngOnInit() {
    this.postService.clearPosts();
    console.log(this.posts);
    if (this.isProfilePage) {
      this.userPic = await this.imageService.getProfilePicUrl();
    }
    this.fetchPosts(this.currentPage);
    window.addEventListener('scroll', this.scroll, true);
  }

  // ngAfterViewInit() {
  //   window.addEventListener('scroll', this.onScroll.bind(this));
  // }

  ngOnDestroy() {
    //this.postsSubscription?.unsubscribe();
    window.removeEventListener('scroll', this.scroll, true);
  }

  isMyProfile(): boolean {
    if (this.user) {
      const isCurrentUserProfile = this.authService.viewingProfile(this.userId);
      return isCurrentUserProfile;
    }
    return false;
  }

  fetchPosts(page: number): void {
    // If there is a userId, we are on a profile page. Grab only the posts from that userId
    console.log(this.userId);
    console.log('fetching posts at page ' + this.currentPage);
    if (this.router.url != '/home' && this.userId) {
      // if (this.userId) {
      this.isCurrentUserProfile = this.isMyProfile();
      this.postsSubscription = this.postService.posts$.subscribe((posts) => {
        this.posts = posts
          .filter((post) => post.userId === this.userId)
          .map((post) => ({
            ...post,
            userPic: this.user?.userPic,
          }));
      });
      this.postService.fetchPostsByUserId(this.userId, this.currentPage);
    } else if (this.router.url == '/home') {
      // Default behavior, get all posts
      this.postService.fetchPosts(this.currentPage);
    }
  }

  scroll = (event: any): void => {
    console.log('onScroll triggered');
    // Vertical pixels scrolled
    const scrolled = window.scrollY;
    // Full height of the window's content
    const fullHeight = document.documentElement.scrollHeight;
    // Height of the visible window
    const windowHeight = window.innerHeight;
    // Check if we've scrolled to the bottom
    if (Math.ceil(scrolled + windowHeight) >= fullHeight) {
      this.currentPage++;
      console.log('Calling fetchPosts for page:', this.currentPage);
      this.fetchPosts(this.currentPage);
    }
  };
}
