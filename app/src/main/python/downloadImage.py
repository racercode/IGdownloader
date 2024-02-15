from dotenv import load_dotenv
from instaloader import Instaloader, Post
import os

def login_instagram(USERNAME: str, PASSWORD: str) -> Instaloader:
    load_dotenv()
    L = Instaloader()
    L.login(USERNAME, PASSWORD)
    return L


def getLink(url: str, USERNAME: str, PASSWORD: str) -> list:
    shortCode = url.split('/')[4]
    L = Instaloader(USERNAME, PASSWORD)
    for _ in range(5):
        try:
            post = Post.from_shortcode(L.context, shortCode)
            urllists = []
            if post.is_video:
                urllists = [post.video_url]
            else:
                if post.typename == 'GraphSidecar':
                    for node in post.get_sidecar_nodes():
                        if node.is_video:
                            urllists.append(node.video_url)
                        else:
                            urllists.append(node.display_url)
                else:
                    urllists.append(post.display_url)

            return urllists

        except:
            L = login_instagram()
    raise ValueError('Error in getLink')


