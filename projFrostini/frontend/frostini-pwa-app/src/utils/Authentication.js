function isAuthenticated(auth) {
    console.log(auth);
    console.log(Object.keys(auth).length);
    if(Object.keys(auth).length > 0) {
        return true;
    }
    return false;
}

export default isAuthenticated;