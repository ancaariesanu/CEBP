class UserClass {
    constructor() {
      if (UserClass.instance) {
        return UserClass.instance;
      }
  
      this.user = null; // Holds user information
      UserClass.instance = this;
    }
  
    setUser(user) {
      this.user = user; // Set the user information
    }
  
    getUser() {
      return this.user; // Retrieve the current user
    }
  
    clearUser() {
      this.user = null; // Clear the user information
    }
  }
  
  const instance = new UserClass();
  export default instance;
  