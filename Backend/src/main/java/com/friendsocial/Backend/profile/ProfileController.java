package com.friendsocial.Backend.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This will have all of the resources for the API

// API LAYER
// -

// ProfileController is used, but you need to go to that URL.
@RestController
@RequestMapping(path="api/v1/profiles")
public class ProfileController {

  private final ProfileService profileService;

  @Autowired
  public ProfileController(ProfileService profileService) {
    /*
      This here should be avoided in favor of dependency injection.
      this.profileService = new ProfileService();

      @Autowired above instantiates a profileService and injects it into the constructor.
     */
    this.profileService = profileService;
  }

  // GET ALL PROFILES
  // Get mapping because we want to get something out from our server
  @GetMapping()
  public List<Profile> getProfiles() {
    return profileService.getProfiles();
  }

  // GET ONE PROFILE
  @GetMapping(path = "{profileId}")
  public Profile getOneProfileById(@PathVariable("profileId") Long id) {
    return profileService.getProfileById(id);
  }

  // POST (ADD) A PROFILE
  // @RequestBody because we are taking the profile that comes from the client. Take request and map to profile
  @PostMapping
  public void registerNewProfile(@RequestBody Profile profile) {
    profileService.addNewProfile(profile);
  }

  // DELETE A PROFILE
  // pass the profileId within the path. Grab the student ID with @PathVariable
  @DeleteMapping(path = "{profileId}")
  public void deleteProfile(@PathVariable("profileId") Long id) {
    profileService.deleteProfile(id);
  }

  @PutMapping(path = "{profileId}")
  public void updateProfile(
          @PathVariable("profileId") Long id,
          @RequestParam(required = false) String email,
          @RequestParam(required = false) String firstName) {
    profileService.updateProfile(id, email, firstName);
  }


}
