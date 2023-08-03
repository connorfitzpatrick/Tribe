import { Component, OnInit } from '@angular/core';
import { ProfileService } from './services/ProfileService';
import { AuthService } from './services/AuthService';
import { User } from './models/profile.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private profileService: ProfileService
  ) {}

  title = 'Friends150';

  ngOnInit(): void {
    const username = this.authService.getUsername();
    // Fetch the user data and update the currentUserSubject
    this.profileService.fetchLoggedInUserData(username).subscribe(
      (user: User) => {
        this.authService.currentUserSubject.next(user);
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );
  }
}
