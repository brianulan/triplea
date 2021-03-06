package games.strategy.engine.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import games.strategy.triplea.delegate.TechAdvance;

public class TechnologyFrontierList extends GameDataComponent {
  private static final long serialVersionUID = 2958122401265284935L;
  private final List<TechnologyFrontier> m_technologyFrontiers = new ArrayList<>();

  public TechnologyFrontierList(final GameData data) {
    super(data);
  }

  public void addTechnologyFrontier(final TechnologyFrontier tf) {
    m_technologyFrontiers.add(tf);
  }

  public void addTechnologyFrontiers(final Collection<TechnologyFrontier> technologyFrontiers) {
    checkNotNull(technologyFrontiers);
    m_technologyFrontiers.addAll(technologyFrontiers);
  }

  public int size() {
    return m_technologyFrontiers.size();
  }

  public TechnologyFrontier getTechnologyFrontier(final String name) {
    for (final TechnologyFrontier tf : m_technologyFrontiers) {
      if (tf.getName().equals(name)) {
        return tf;
      }
    }
    return null;
  }

  public List<TechAdvance> getAdvances() {
    final List<TechAdvance> techs = new ArrayList<>();
    for (final TechnologyFrontier t : m_technologyFrontiers) {
      techs.addAll(t.getTechs());
    }
    return techs;
  }

  public List<TechnologyFrontier> getFrontiers() {
    return Collections.unmodifiableList(m_technologyFrontiers);
  }

  public boolean isEmpty() {
    return size() == 0;
  }
}
